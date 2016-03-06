
'use strict';

angular.module('feedAggregation.Landing', ['ui.router'])
    .controller("LandingCtrl", function($scope, $state){
        $scope.next = function(){
            $state.go("gallery");
        }
    });
