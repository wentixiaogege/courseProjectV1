$.getScript("js/config.js", function() {
    console.log("config.js loaded");
})

function DispalyObj(name, min, max, colors, vAxisName, field) {
    this.name = name;
    this.min = min;
    this.max = max;
    this.colors = colors;
    this.vAxisName = vAxisName;
    this.chartDivName = "chart_" + name;
    this.data_field = field;
}

// device object.
function Device(deviceId, deviceName, deviceType) {
    this.deviceId = deviceId;
    this.deviceName = deviceName;
    this.deviceType = deviceType;
    this.props = [];
    this.temp_head = '<h3>' + deviceName + '</h3> <div class="hideOverflow">';
    this.temp_tail = "</div>"
}

//DeviceProperty obj. every device has many deviceobj.
function DeviceProperty(fullName, datas) {
    var index = fullName.indexOf("_");
    this.propName = fullName.substring(index + 1);
//    this.times = times;
    this.datas = datas;
    this.ptype = fullName.substring(0, index);
    this.isDisplay = false;
}

// set DeviceProperties to device
Device.prototype.setPropData = function(deviceProperty) {
    this.props.push(deviceProperty);
}

// prepare for display.
Device.prototype.initDisplay = function() {

    this.template = this.temp_head;
    for (var i = 0; i < this.props.length; i++) {

        // get the property for every device
        var prop = this.props[i];
        var ptype = prop.ptype;

        if ('VOLTAGE' === ptype || 'CURRENT' === ptype || 'TEMP' === ptype)
            prop.isDisplay = true;

        if (prop.isDisplay) {
            var display;
            if ('VOLTAGE' === ptype) {
                display = new DispalyObj(this.deviceName + "_" + prop.propName, 0, 200, ['#3366CC'], 'Volt', prop.propName);
            } else if ('CURRENT' === ptype) {
                display = new DispalyObj(this.deviceName + "_" + prop.propName, 100, 300, ['#DC3912'], 'mAbe', prop.propName);
            } else {
                display = new DispalyObj(this.deviceName + "_" + prop.propName, 0, 100, ['#FCD209'], 'C', prop.propName);
            }
            // 1. add display property
            prop.display = display;
            // 2. add template property
            prop.template = ' <div id="' + display.chartDivName + '" class="chart-div"></div>';


            // 4. if this property will be displayed, add to device template
            if (prop.isDisplay)
                this.template += prop.template;

            // every property has display data. it is a array like [[time,value,'value',comment], [time,value,'value',comment]]
            // I will construct it.
//            var len = prop.times.length;
            var displayData = [];
            for (var j = 0; j < len; j++) {
                var info = [];
                var date = new Date(prop.datas[j].timestamp);
                var value = prop.datas[j].data;

                //for test
                // var value = 60;

                info.push(date);
                info.push(value);
                info.push(value + "");
                var clor = prop.display.colors[0];
                info.push("<span style='font-size:20px;'>" + formatTime(date) + " <b style='color:" + clor + "'>Value:" + value + "</b></span>");
                displayData.push(info);
            };

            //5 .add display data property
            prop.displayData = displayData;
        }
    };
    this.template += this.temp_tail;
}


var app = angular.module('myApp', []);

/**
 * ChartController control the chart display
 */
app.controller('ChartController', ['$scope', '$http', '$interval', function($scope, $http, $interval) {

    console.log("ChartController initing...");
    $scope.playing = false;

    // the information of smartmeter
    //    $scope.SmartMeters = ItuConfiguration.SmartMeters;
    $scope.Devices = ItuConfiguration.Devices;
    $scope.SearchPeriods = ItuConfiguration.SearchPeriods;
    $scope.Intervals = ItuConfiguration.Intervals;
    //    $scope.Servers = ItuConfiguration.Servers;
    // current information
    //    $scope.smartmeter = $scope.SmartMeters[0];
    $scope.device = $scope.Devices[1];
    $scope.peroid = $scope.SearchPeriods[1];
    $scope.interval = $scope.Intervals[2];
    //    $scope.server = $scope.Servers[0];
    $scope.startDate = "";
    $scope.endDate = "";
    // searchTimeType: 0: peroid, 1, date range.
    $scope.searchTimeType = {
        val: "1",
        desp: "Period",
        per_desc: "Period Picker",
        date_desc: "Date Picker"
    };

    //errors
    $scope.errors = [];
    $scope.AccordionContents = [];
    $scope.loads = [];
    $scope.devices = [];

    $scope.changeSearTime = function(val) {
        if (val === "0") {
            $("#spanDatePicker").show();
            $("#searchPeriod").hide();
            $scope.searchTimeType.desp = "Date";
            // $("#thTimeSpan").text('Date');
        } else if (val === "1") {
            $("#searchPeriod").show();
            $("#spanDatePicker").hide();
            $scope.searchTimeType.desp = "Period";
            // $("#thTimeSpan").text('Period');
        }
    }

    //accordion
    $("#accordion").accordion({
        heightStyle: "content"
    });

    // date picker
    $("#datefrom").datepicker({
        defaultDate: "+1w",
        changeMonth: true,
        numberOfMonths: 1,
        onClose: function(selectedDate) {
            $("#dateto").datepicker("option", "minDate", selectedDate);
        },
        onSelect: function(dateText, inst) {
            console.log(dateText);
            // $scope.searchParam.startDate = dateText;
            $scope.startDate = dateText;
        }
    });
    $("#dateto").datepicker({
        defaultDate: "+1w",
        changeMonth: true,
        numberOfMonths: 1,
        onClose: function(selectedDate) {
            $("#datefrom").datepicker("option", "maxDate", selectedDate);
        },
        onSelect: function(dateText, inst) {
            console.log(dateText);
            // $scope.searchParam.endDate = dateText;
            $scope.endDate = dateText;
        }
    });

    //draw chart for devices.
    $scope.drawDevicesChart = function() {
        for (var i = 0; i < $scope.devices.length; i++) {
            var device = $scope.devices[i];
            $("#accordion").append(device.template);
            for (var j = 0; j < device.props.length; j++) {
                var prop = device.props[j];
                if (prop.isDisplay)
                    drawChart(prop, prop.display);
            };
        };
    };
    /*
     *   construct $scope.device
     */
    $scope.constructDevice = function(data) {
        $scope.devices = [];
        //1. get all devices
        var dataLength = data.length;

        var device = new Device($scope.device.id, "Jack Test Device Name", "TEMP");
        
        var deviceProperty = new DeviceProperty("TEMP", data);
        
        device.setPropData(deviceProperty);
        
        for (var j = 0; j < data.length; j++) {
            var prop = deviceData[j];
           
           
        };
        device.initDisplay();
        // $("#accordion").append(device.template);
        $scope.devices.push(device);
        
    };
    /*
     *   construct $scope.devices
     */
    $scope.constructDevices = function(data) {
        $scope.devices = [];
        //1. get all devices
        var devicesAmount = data.length;

        //loop for every device
        for (var i = 0; i < devicesAmount; i++) {
            var d = data[i];
            var device = new Device(d.device_id, d.device_name, d.device_type);
            var deviceData = d.deviceData;

            for (var j = 0; j < deviceData.length; j++) {
                var prop = deviceData[j];
                var deviceProperty = new DeviceProperty(prop.realName, d.devicetime, prop.fieldData);
                device.setPropData(deviceProperty);
            };
            device.initDisplay();
            // $("#accordion").append(device.template);
            $scope.devices.push(device);
        };
    };

    /*
     * http request
     */
    $scope.doRequest = function(url) {
        $http({
            method: 'GET',
            url: url,
            headers: {
                'Content-Type': 'application/json'
            },
            data: $scope.getSearchParam()
        }).success(function(data, status, headers, config) {
            console.log(JSON.stringify(data));
            console.log(status);
            //init data
            $("#accordion").empty();

            $scope.constructDevice(data);
            $scope.drawDevicesChart();

            $("#accordion").accordion("refresh");
            $("#accordion").accordion("option", "active", 0);

        }).
        error(function(data, status, headers, config) {
            console.log(data);
            $scope.errors = [];
            $scope.errors.push(data);
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });
    };

    $scope.getSearchParam = function() {
        return {
/*          server: $scope.server.id,
            smId: $scope.smartmeter.id,
            peroid: $scope.peroid.id,
            startDate: $scope.startDate,
            endDate: $scope.endDate,
            interval: $scope.interval.id,
            // interval: 0,
            searchTimeType: $scope.searchTimeType.val*/
            
            intervals: $scope.interval.id,
            starttime: $scope.startDate,
            endtime: $scope.endDate
        };
    };

    $scope.confirm = function() {
        console.log("submitting");
        console.log($("#datefrom").val());
        if ($scope.docheck()){
        	  console.log("confirm $scope.deviceis  "+$scope.device);
        	  $scope.doRequest($("#hidRoot").val() + "service/devices/1/peroid/temp");
        	  console.log($scope.getSearchParam());
        }
          
    }

    $scope.docheck = function() {
        if ($scope.searchTimeType.val === "0") {
            if ($scope.startDate === "" || $scope.endDate === "") {
                $scope.errors = [];
                $scope.errors.push("either startDate or endDate can not be null!");
                return false;

            }
        }
        if ($scope.searchTimeType.val === "1") {
            if ($scope.startDate === "" || $scope.endDate === "") {
                $scope.errors = [];
                $scope.errors.push("either startDate or endDate can not be null!");
                return false;

            }
        }
        return true;
    };

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

    $scope.startServer = function() {
        $scope.confirm();
        // stop = $interval(function() {
        //     $scope.confirm();
        // }, 15000);
        // $scope.stopTest = "Stop";
    }

    //frist time call ?
    $scope.startServer();
}]);

/*
 *	draw chart for data.
 */
function drawChart(content, obj) {
    var data = new google.visualization.DataTable();
    data.addColumn('datetime', 'Time');
    data.addColumn('number', obj.name);
    // data.addColumn({type:'string', role:'tooltip', p: {html:true}});
    data.addColumn({
        type: 'string',
        role: 'annotation'
    });
    data.addColumn({
        type: 'string',
        role: 'annotationText',
        p: {
            html: true
        }
    });

    // data.addRows(obj.getData(content));
    data.addRows(content.displayData);
    console.log("accordion width:" + $('#accordion').width());
    console.log("chartDiv width:" + $('#chartDiv').width());
    var chart_width = $('#chartDiv').width() - 38;
    var options = {
        chart: {
            title: 'The Chart of ' + obj.name
                // subtitle: 'in millions of dollars (USD)'
        },
        // curveType: 'function',
        legend: {
            // position: 'left'
        },
        min: 0,
        max: obj.max,
        // hAxis.format:'MM-dd HH:mm',
        // displayAnnotations: false,
        // legend: 'none',
        tooltip: {
            isHtml: true,
            trigger: 'selection'
        },
        explorer: {
            axis: 'horizontal',
            keepInBounds: true,
            maxZoomOut: 1
        },
        colors: obj.colors,
        // // scaleColumns: [2, 3],
        // legendPosition: 'newRow',
        // // colors: ['red'],
        // dateFormat: "MM-dd HH:mm",
        hAxis: {
            format: 'MM-dd HH:mm',
            title: 'Time'
        },
        vAxis: {
            title: obj.vAxisName
        },
        legend: {
            position: 'top',
            alignment: 'center'
        },
        width: chart_width,
        height: 300
    };

    var chart = new google.visualization.LineChart(document.getElementById(obj.chartDivName));
    chart.draw(data, options);

}