var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "370",
        "ok": "0",
        "ko": "370"
    },
    "minResponseTime": {
        "total": "0",
        "ok": "-",
        "ko": "0"
    },
    "maxResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "meanResponseTime": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "standardDeviation": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles1": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles2": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "percentiles3": {
        "total": "8",
        "ok": "-",
        "ko": "8"
    },
    "percentiles4": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 370,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "6.167",
        "ok": "-",
        "ko": "6.167"
    }
},
contents: {
"req_get--api-etudia-281200445": {
        type: "REQUEST",
        name: "GET /api/etudiants",
path: "GET /api/etudiants",
pathFormatted: "req_get--api-etudia-281200445",
stats: {
    "name": "GET /api/etudiants",
    "numberOfRequests": {
        "total": "50",
        "ok": "0",
        "ko": "50"
    },
    "minResponseTime": {
        "total": "0",
        "ok": "-",
        "ko": "0"
    },
    "maxResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "meanResponseTime": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "standardDeviation": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles1": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles2": {
        "total": "4",
        "ok": "-",
        "ko": "4"
    },
    "percentiles3": {
        "total": "4",
        "ok": "-",
        "ko": "4"
    },
    "percentiles4": {
        "total": "10",
        "ok": "-",
        "ko": "10"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 50,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.833",
        "ok": "-",
        "ko": "0.833"
    }
}
    },"req_get--api-etudia--1475570905": {
        type: "REQUEST",
        name: "GET /api/etudiants - Before Create",
path: "GET /api/etudiants - Before Create",
pathFormatted: "req_get--api-etudia--1475570905",
stats: {
    "name": "GET /api/etudiants - Before Create",
    "numberOfRequests": {
        "total": "320",
        "ok": "0",
        "ko": "320"
    },
    "minResponseTime": {
        "total": "0",
        "ok": "-",
        "ko": "0"
    },
    "maxResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "meanResponseTime": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "standardDeviation": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles1": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "percentiles2": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "percentiles3": {
        "total": "9",
        "ok": "-",
        "ko": "9"
    },
    "percentiles4": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 320,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "5.333",
        "ok": "-",
        "ko": "5.333"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
