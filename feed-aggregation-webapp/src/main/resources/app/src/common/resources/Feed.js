'use strict';

angular.module('feedAggregation.Resources.Feed', ['BaseResource'])
    .factory("Feed", function(BaseResource){
        var url = "/content",
            params = {
                id: "@id"
            },
            Feed = BaseResource.create(url, params, {});
        return Feed;
    });

