cordova.define("cordova-plugin-appsapi.AppsApi", function(require, exports, module) {
 var appAvailability = {
    
    check: function(urlScheme, successCallback, errorCallback) {
        cordova.exec(
            successCallback,
            errorCallback,
            "AppsApi",
            "checkAvailability",
            [urlScheme]
        );
    },
    
    checkBool: function(urlScheme, callback) {
        cordova.exec(
            function(success) { callback(success); },
            function(error) { callback(error); },
            "AppsApi",
            "checkAvailability",
            [urlScheme]
        );
    },

    startApp : function(intent) {

        var androidIntent = intent;

        // fire
        cordova.exec(
            null,
            null,
            'AppsApi',
            'startActivity', [androidIntent]
        );
    },

    startShortcut : function(url, title, imgBase64) {
        cordova.exec(
              null,
              null,
              'AppsApi',
              'startShortcut', [url, title, imgBase64]
          );
    },

    startUrl : function(url) {
        cordova.exec(
              null,
              null,
              'AppsApi',
              'startUrl', [url]
          );
    },

    bindFavoriteApp : function() {
        cordova.exec(null, null, 'AppsApi', 'bindFavoriteApp', []);
    }
    
};

module.exports = appAvailability;
});
