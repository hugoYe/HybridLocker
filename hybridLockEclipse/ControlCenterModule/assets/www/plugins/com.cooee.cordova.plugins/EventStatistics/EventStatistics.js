cordova.define("cordova-plugin-eventstatistics.EventStatistics", function(require, exports, module) {
 var EventStatistics = {

    clickApp : function(){
        console.log("clickApp-js111")
        cordova.exec(
            null,
            null,
            'EventStatistics',
            'clickApp',[]
        );
    },
    openCentrolCenter : function() {
        console.log("openCentrolCenter-js111")
        cordova.exec(
            null,
            null,
            'EventStatistics',
            'openCentrolCenter', []
        );
    },

};

module.exports = EventStatistics;
});