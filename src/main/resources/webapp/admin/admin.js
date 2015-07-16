angular.module('admin', ['ui.bootstrap', 'dialogs.main', 'pascalprecht.translate', 'dialogs.default-translations']);
angular.module('admin').controller('SmartMeterLightCtrl', function($scope, $interval, $http, $window, dialogs) {

    $scope.alerts = [];
    // which server interface will be called. call from localServer or DataServer.
    $scope.serverModel = "localServer";

    // $scope.doAjaxRequest = function(url, method, data){}

    $scope.doRequest = function(url, server) {
        if (server === undefined)
            server = $scope.serverModel;

        $http({
            method: 'GET',
            // url: 'testdata2.json',
            url: '../front/lightssearchaction?server=' + server,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
            // data: $.param($scope.getSearchParam())
        }).success(function(data, status, headers, config) {
            console.log(data);
            $scope.pri_smartmeters = data;
            $scope.smartmeters = addDispalyOptions(data);
            console.log($scope.smartmeters);
        }).
        error(function(data, status, headers, config) {
            console.log("errors");
            console.log(status);
            console.log(data);
            $scope.alerts = [];
            $scope.alerts.push({
                msg: data.ERROR_CODE,
                "type": "danger"
            });
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });
    };

    $scope.go = function(sm) {

        console.log("go");
        console.log(sm.coordinator_id);
        console.log(sm.sm_id);
        console.log(sm.sm_name);
        // console.log($("#testsel").length);
        // $("#testsel").bootstrapSwitch();
    }

    $scope.changeServer = function(mModel) {
        console.log(mModel);
        if ($scope.serverModel === mModel) {
            return;
        } else {
            console.log("call back to server");
            $scope.doRequest("", mModel);
        }

    }

    $scope.change_temp = function(sm, light, min_temp, max_temp) {
        // $window.alert("ok");
        // dialogs.notify('Something Happened!','Something happened that I need to tell you.');



        $scope.alerts = [];

        if ($window.isNaN(min_temp) || $window.isNaN(max_temp)) {
            $scope.alerts.push({
                msg: "temprature must be a number",
                "type": "danger"
            });
            return;
        }

        if (Number(min_temp) >= Number(max_temp)) {
            $scope.alerts.push({
                msg: "min temprature must be not bigger than max temprature",
                "type": "danger"
            });
            return;
        }

        var dlg = dialogs.confirm("Change Temprature", "Do you want to change min and max temprature?");
        dlg.result.then(function() {
            $http({
                method: 'POST',
                url: '../front/tempraturecontrolaction/json',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    'server': $scope.serverModel,
                    'operation': {
                        "coordinator_id": sm.coordinator_id,
                        "sm_id": sm.sm_id,
                        "device_id": light.device_id,
                        "max_value": max_temp,
                        "min_value": min_temp
                    }
                }
                // data: $.param($scope.getSearchParam())
            }).success(function(data, status, headers, config) {
                console.log(data);
                // $scope.confirmed = 'You confirmed "Yes."';
                light.min_value = min_temp;
                light.max_value = max_temp;
            }).
            error(function(data, status, headers, config) {
                console.log("errors");
                console.log(status);
                $scope.alerts = [];
                $scope.alerts.push({
                    msg: data.ERROR_CODE,
                    "type": "danger"
                });
            });

        }, function() {
            console.log("not change");
            // $scope.confirmed = 'You confirmed "No."';
        });

    }

    $scope.change_light = function(sm, light, status) {
        // var light_status = status.toString() === "1" ? '0' : '1';
        // light.status = light_status;

        //if light is on, shut down the light
        //if light is off, turn on the light
        var manipulation = status.toString() === "1" ? '0' : '1';

        var msg = "do you want to " + (manipulation === "0" ? " shut down " : " open ") + "light?";
        var dlg = dialogs.confirm("Lights", msg);
        dlg.result.then(function() {
            $http({
                method: 'POST',
                url: '../front/lightmanipulationaction/json',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: {
                    // "coordinator_id": sm.coordinator_id,
                    // "sm_id": sm.sm_id,
                    // "load_id": light.load_id,
                    // "load_config_id": light.load_config_id,
                    // "manipulation": manipulation
                    'server': $scope.serverModel,
                    "manipulation": {
                        "coordinator_id": sm.coordinator_id,
                        "sm_id": sm.sm_id,
                        "device_id": light.device_id,
                        "manipulation": manipulation
                    }
                }
                // data: $.param($scope.getSearchParam())
            }).success(function(data, status, headers, config) {
                console.log(data);
                light.device_status = manipulation;
                // $scope.confirmed = 'You confirmed "Yes."';
                light.light_class = light.device_status === "1" ? "btn-success" : "btn-default";
                light.light_disp = light.device_status === "1" ? "ON" : "OFF";
                light.light_pic_class = light.device_status === "1" ? "light_on" : "light_off";
            }).
            error(function(data, status, headers, config) {
                console.log("errors");
                console.log(status);
                $scope.alerts = [];
                $scope.alerts.push({
                    msg: data.ERROR_CODE,
                    "type": "danger"
                });
                // $scope.errors = [];
                // $scope.errors.push(data);
            });

        }, function() {
            console.log("not change");
            // $scope.confirmed = 'You confirmed "No."';
        });


    }


    // $scope.doRequest("", $scope.serverModel);
    var stop;

    $scope.stopTest = "Stop";
    $scope.stop_refresh = function() {
        if (angular.isDefined(stop)) {
            $interval.cancel(stop);
            stop = undefined;
            $scope.stopTest = "Start";
        } else {
           $scope.startServer();
        }
    }


    // var stopTime = $interval($scope.doRequest("", $scope.serverModel), 1000);
    $scope.startServer = function() {
        $scope.doRequest("", $scope.serverModel);
        stop = $interval(function() {
            // console.log("ok");
            $scope.doRequest("", $scope.serverModel);
        }, 15000);
		$scope.stopTest = "Stop";
    }

    $scope.startServer();
});

function addDispalyOptions(meters) {

    var smartmeters = [];
    if (Object.prototype.toString.call(meters) === '[object Array]') {
        smartmeters = meters;
    } else {
        smartmeters.push(meters);
    }


    var len = smartmeters.length;

    for (var i = 0; i < len; i++) {
        var smartmeter = smartmeters[i];
        //that is a bug for tomcat. when lightDatas has only on item , tomcat will cast it to a Object.
        if ("[object Object]" === Object.prototype.toString.call(smartmeter.lightDatas)) {
            var lightDatas = [];
            lightDatas.push(smartmeter.lightDatas);
            smartmeter.lightDatas = lightDatas;
        }

        for (var j = 0; j < smartmeter.lightDatas.length; j++) {
            var light = smartmeter.lightDatas[j];
            // add property light_class and light_disp
            var light_status = light.device_status.toString();
            light.light_class = light_status === "1" ? "btn-success" : "btn-default";
            light.light_disp = light_status === "1" ? "ON" : "OFF";
            light.light_pic_class = light_status === "1" ? "light_on" : "light_off";
        };
    };

    return smartmeters;
}
