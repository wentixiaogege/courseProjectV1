angular.module('admin', ['ui.bootstrap', 'dialogs.main', 'pascalprecht.translate', 'dialogs.default-translations']);
angular.module('admin').controller('SmartMeterLightCtrl', function($scope, $interval, $http, $window, dialogs) {

    $scope.alerts = [];
    // which server interface will be called. call from localServer or DataServer.

    // get all devices
    $scope.doRequest = function(url) {

    	/* $http({
             method: 'GET',
             // url: 'testdata2.json',
             url: "../service/devices/",//'../front/lightssearchaction?server=' + server,
             headers: {
                 'Content-Type': 'application/json'
             }
             // data: $.param($scope.getSearchParam())
         })*/
    	$http.get("../service/devices/").success(function(data, status, headers, config) {
            console.log(data);
            $scope.pri_smartmeters = data;
            $scope.devices = addDispalyOptions(data);
            console.log($scope.devices);
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

    $scope.relay_device = function(device, status) {
        // var light_status = status.toString() === "1" ? '0' : '1';
        // light.status = light_status;

        //if light is on, shut down the light
        //if light is off, turn on the light
        var manipulation = status.toString() === "1" ? '0' : '1';

        var msg = "do you want to " + (manipulation === "0" ? " shut down " : " open ") + "light?";
        var dlg = dialogs.confirm("Lights", msg);
        dlg.result.then(function() {
        	/*({
                method: 'POST',
                url: '../service/devices/'+device.id+'/'+manipulation,
                headers: {
                    'Content-Type': 'application/json'
                },
            })*/
            $http.post('../service/devices/'+device.id+'/'+manipulation)
            .success(function(data, status, headers, config) {
                console.log(data);
                device.device_status = manipulation;
                // $scope.confirmed = 'You confirmed "Yes."';
                device.device_class = device.device_status === "1" ? "btn-success" : "btn-default";
                device.device_disp = device.device_status === "1" ? "ON" : "OFF";
                device.device_pic_class = device.device_status === "1" ? "device_on" : "device_off";
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
        $scope.doRequest("");
        stop = $interval(function() {
            $scope.doRequest("");
        }, 15000);
		$scope.stopTest = "Stop";
    }

    $scope.startServer();
});

function addDispalyOptions(getdevices) {

    var devices = [];
    if (Object.prototype.toString.call(getdevices) === '[object Array]') {
        devices = getdevices;
    } else {
        devices.push(getdevices);
    }


    var len = devices.length;

    for (var i = 0; i < len; i++) {
        var device = devices[i];
        
        device.device_status = device.status.toString();
        device.device_class = device.device_status === "1" ? "btn-success" : "btn-default";
        device.device_disp = device.device_status === "1" ? "ON" : "OFF";
        device.device_pic_class = device.device_status === "1" ? "device_on" : "device_off";
        //that is a bug for tomcat. when lightDatas has only on item , tomcat will cast it to a Object.
      /*  if ("[object Object]" === Object.prototype.toString.call(smartmeter.lightDatas)) {
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
            light.light_pic_class = light_status === "1" ? "device_on" : "device_off";
        };*/
    };

    return devices;
}
