'use strict';

/**
 * quick module and directive that can be reused. - see usage in gallery.js & gallery.tpl.html
 */
angular.module('Common.Modal', [])
    .directive('modal', function () {
    return {
        template: '<div class="modal fade">' +
                    '<div class="modal-dialog">' +
                        '<div class="modal-content">\n   ' +
                            '<div class="modal-header">\n       ' +
                                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>\n       ' +
                                '<h4 class="modal-title">{{ title }}</h4>' +
                            '</div>\n   ' +
                        '<div class="modal-body" ng-transclude></div>' +
                        '</div>' +
                    '</div>' +
                '</div>',
        restrict: 'E',
        transclude: true,
        replace:true,
        scope:true,
        link: function postLink(scope, element, attrs) {
            scope.title = attrs.title;

            scope.$watch(attrs.visible, function(value){
                if(value == true)
                    $(element).modal('show');
                else
                    $(element).modal('hide');
            });

            $(element).on('shown.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = true;
                });
            });

            $(element).on('hidden.bs.modal', function(){
                scope.$apply(function(){
                    scope.$parent[attrs.visible] = false;
                });
            });
        }
    };
});