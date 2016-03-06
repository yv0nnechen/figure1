
'use strict';

angular.module('feedAggregation.Gallery', ['feedAggregation.Resources.Feed'])
    .controller("GalleryCtrl", function($scope, Feed){
        $scope.feeds = Feed.query();
        $scope.assets = [
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
            {id:1},
        ]
    });
