'use strict';

// Declare app level module which depends on views, and components
angular.module('feedAggregation', [
    'ui.router',
    'feedAggregation.Landing',
    'feedAggregation.Gallery'
])
    .config(function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise(function($injector) {
            //for some reason, the otherwise url causes infinite $digest loop
            var $state = $injector.get("$state");

            $state.go("landing");
        });

        //placeholder for auth interceptor so that requests can be sent with auth token.
        //$httpProvider.interceptors.push("AuthInterceptor");

        $stateProvider
            .state('publicRoot', {
                abstract:true,
                template:"<ui-view></ui-view>",
                data:{
                    requireLogin: false
                }
            })
            .state('privateRoot', {
                abstract: true,
                template:"<ui-view></ui-view>",
                data: {
                    requireLogin: true
                }
            })
            .state('landing', {
                parent: 'publicRoot',
                url:'/login',
                templateUrl: 'src/app/landing_component/landing.tpl.html',
                controller: "LandingCtrl"
            })
            .state('gallery', {
                parent: 'privateRoot',
                url:'/gallery',
                templateUrl: 'src/app/gallery/gallery.tpl.html',
                //controller: "GalleryCtrl"
            })


    });