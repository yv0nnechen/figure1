'use strict';

angular.module('feedAggregation.Resources.Feed', ['BaseResource'])
    .factory("InstagramFeed", function(BaseResource){
        var url = "/content/instagram/:id",
            params = {
                id: "@id"
            },
            Feed = BaseResource.create(url, params, {});
        return Feed;
    })
    .factory("PixelFeed", function(BaseResource){
        var url = "/content/pixel/:id",
            params = {
                id: "@id"
            },
            Feed = BaseResource.create(url, params, {});
        return Feed;
    });

