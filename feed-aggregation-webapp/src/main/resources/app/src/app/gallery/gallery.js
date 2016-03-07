
'use strict';

angular.module('feedAggregation.Gallery', ['feedAggregation.Resources.Feed', 'Common.Modal'])
    .controller("GalleryCtrl", function($scope, InstagramFeed, PixelFeed, isInstagramAuthenticated){
        $scope.instagramAuthenticated = isInstagramAuthenticated;
        $scope.mergedFeeds = [];
        InstagramFeed.get()
            .$promise
            .then(function(paginatedFeeds){
                $scope.mergedFeeds=$scope.mergedFeeds.concat(paginatedFeeds.feeds);
            })
            .catch(function(error){
                if(error.status === 401){
                    $scope.instagramAuthenticated = false;
                } else {
                    //TODO Notification
                }
            });
        PixelFeed.get()
            .$promise
            .then(function(paginatedFeeds){
                $scope.mergedFeeds=$scope.mergedFeeds.concat(paginatedFeeds.feeds);
            })
            .catch(function(error){
                //TODO Notification
            });

        $scope.authInst = function(){
            if (!$scope.instagramAuthenticated) window.location.replace("/auth/inst/step1");
        };

        $scope.showModal = false;
        $scope.toggleModal = function(feed){
            if(feed.contentType === "INSTAGRAM"){
                $scope.individual = InstagramFeed.get({id: feed.id});
            } else if (feed.contentType === "PIXEL"){
                $scope.individual = PixelFeed.get({id: feed.id});
            } else {
                return;
            }
            $scope.showModal = !$scope.showModal;
        };



    });
