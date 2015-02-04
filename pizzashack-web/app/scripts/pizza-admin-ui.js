'use strict';

angular.module('pizzaAdmin')
.directive('pizzaList',function(){
        return {
            restrict: 'E',
            transclude: true,
            scope: {
            },
            template: '<th style="font-weight:normal;">{{val.pizzashackId}}</th><th style="font-weight:normal;">{{val.pizzaName}}</th><th style="font-weight:normal;">$ {{val.price}}</th><th><a href="#/{{val.pizzashackId}}" class="btn btn-info" role="button">Info</a> <button type="button" class="btn btn-primary">Edit</button><button type="button" class="btn btn-warning">Delete</button></th>'

        };
});