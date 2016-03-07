
'use strict';

angular.module('feedAggregation.Gallery', ['feedAggregation.Resources.Feed', 'Common.Modal'])
    .controller("GalleryCtrl", function($scope, InstagramFeed, PixelFeed, isInstagramAuthenticated){
        $scope.instagramAuthenticated = isInstagramAuthenticated;

        $scope.mergedFeeds = [];
        var pagination = {
            pixelPage: 1,
            instaMinId: null,
            instaMaxId: null
        };
        loadPage();

        $scope.authInst = function(){
            if (!$scope.instagramAuthenticated) window.location.replace("/auth/inst/step1");
        };

        $scope.prevPage = function(){
            if(pagination.pixelPage > 1){
                resetPage();
                loadPage({
                    instagram: {
                        min_id: pagination.instaMinId
                    },
                    pixel: {
                        page: pagination.pixelPage -1
                    }
                });
            }
        };

        $scope.nextPage = function(){
            resetPage();
            loadPage({
                instagram: {
                    max_id: pagination.instaMaxId
                },
                pixel: {
                    page: pagination.pixelPage +1
                }
            });
        };

        //disable prev

        function resetPage(){
            //clear page
            $scope.mergedFeeds.length = 0;
        }

        function loadPage(params){
            params = params || {};
            var instagramParams = params.instagram||{};
            var pixelParams = params.pixel||{};

            InstagramFeed.get(instagramParams)
                .$promise
                .then(function(paginatedFeeds){
                    $scope.mergedFeeds=$scope.mergedFeeds.concat(paginatedFeeds.feeds);
                    pagination.instaMaxId = paginatedFeeds.pagination.maxId;
                    pagination.instaMinId = paginatedFeeds.pagination.minId;
                })
                .catch(function(error){
                    if(error.status === 401){
                        $scope.instagramAuthenticated = false;
                        alert("Reminder - Instagram is not authenticated, please authenticate by clicking the Navbar > Post Source > Instagram");
                    } else {
                        //TODO Notification
                        alert("Encountered server Error");
                    }
                });

            PixelFeed.get(pixelParams)
                .$promise
                .then(function(paginatedFeeds){
                    $scope.mergedFeeds=$scope.mergedFeeds.concat(paginatedFeeds.feeds);
                    pagination.pixelPage = paginatedFeeds.pagination.page;
                    if(paginatedFeeds.pagination.page === 1) {
                        $scope.disablePrev = true;
                    } else {
                        $scope.disablePrev = false;
                    }
                })
                .catch(function(error){
                    //TODO Notification
                    alert("Encountered server Error");
                });
        }


        //TODO would be nice to have the repeated item wrapped in a directive
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
