'use strict';

angular.module('pizzaAdmin')
.directive('pizzaList',function(){
        return {
            scope: {
                pitem: '=pitem'
            },
            template: '<th style="font-weight:normal;">{{pitem.pizzashackId}}</th><th style="font-weight:normal;">{{pitem.pizzaName}}</th><th style="font-weight:normal;">$ {{pitem.price}}</th><th><a href="#/{{pitem.pizzashackId}}" class="btn btn-info" role="button">Info</a> <button type="button" class="btn btn-primary">Edit</button><button type="button" class="btn btn-warning">Delete</button></th>'
        };
});