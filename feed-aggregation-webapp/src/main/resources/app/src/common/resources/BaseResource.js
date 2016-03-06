
'use strict';

angular.module('BaseResource', ['ngResource'])
    .factory('BaseResource', function ($resource) {
        var baseUrl = "";
        return {
            create: function (path, params, actions) {

                //shared params
                var defaultParams = {},
                //shared actions, for example a PUT request.
                    defaultActions = {
                        update: {
                            method: 'PUT'
                        }
                    },
                //shared url prefix
                    url = baseUrl + path;

                return $resource(url,
                    angular.merge({}, defaultParams, params),
                    angular.merge({}, defaultActions, actions));
            }
        };
    });