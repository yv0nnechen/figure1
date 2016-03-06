
'use strict';

angular.module('feedAggregation.Landing', ['ui.router'])
    .controller("LandingCtrl", function($scope, $state){
        $scope.authInstagram = function(){
            $location.go("/auth/inst/step1");
        }
    });
