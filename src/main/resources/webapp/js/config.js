// var SmartMeters = [{
// 	id: "0",
// 	mac: "[0, 18, 75, 0, 4, 15, 26, 60]"
// }, {
// 	id: "1",
// 	mac: "[0, 18, 75, 0, 4, 15, 28, 119]"
// }, {
// 	id: "2",
// 	mac: "[0, 18, 75, 0, 4, 14, -15, -98]"
// }];

ItuConfiguration = (function() {
	return {
		Devices:[{
			id: "0",
			mac: "[0, 12, 4b, 0, 4, 0f, 1a, 3c]"
		}, {
			id: "1",
			mac: "[0, 12, 4b, 0, 4, 0f, 1c, 77]"
		}, {
			id: "2",
			mac: "[0, 12, 4b, 0, 4, 0e, f1, 9e]"
		}, {
			id: "3",
			mac: "[0, 12, 4b, 0, 4, 13, 31, 8e]"
		}, {
			id: "4",
			mac: "[0, 12, 4b, 0, 4, 13, 32, 8a]"
		}, {
			id: "5",
			mac: "[0, 12, 4b, 0, 4, 0e, f3, 91]"
		}],
		SmartMeters: [{
			id: "0",
			mac: "[0, 12, 4b, 0, 4, 0f, 1a, 3c]"
		}, {
			id: "1",
			mac: "[0, 12, 4b, 0, 4, 0f, 1c, 77]"
		}, {
			id: "2",
			mac: "[0, 12, 4b, 0, 4, 0e, f1, 9e]"
		}, {
			id: "3",
			mac: "[0, 12, 4b, 0, 4, 13, 31, 8e]"
		}, {
			id: "4",
			mac: "[0, 12, 4b, 0, 4, 13, 32, 8a]"
		}, {
			id: "5",
			mac: "[0, 12, 4b, 0, 4, 0e, f3, 91]"
		}],
		SearchPeriods: [{
			id: "1h",
			desp: "one hour"
		}, {
			id: "5h",
			desp: "5 hours"
		}, {
			id: "1d",
			desp: "one day"
		}, {
			id: "1w",
			desp: "one week"
		}, {
			id: "1m",
			desp: "one month"
		}, {
			id: "all",
			desp: "all"
		}],
		Intervals: [{
			id: "0",
			desp: "no"
		}, {
			id: "120",
			desp: "two minitues"
		}, {
			id: "600",
			desp: "ten minitues"
		}, {
			id: "3600",
			desp: "one hour"
		}, {
			id: "86400",
			desp: "one day"
		}],
		Servers: [{
			id: "0",
			desp: "localServer"
		}, {
			id: "1",
			desp: "dataServer"
		}]
	};
}());