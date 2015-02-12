'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaCtrl', function ($scope, $resource, pizzaAdminApi) {
      $scope.pizzas = [];
      var update = function(){
        pizzaAdminApi.get().then(function(data){
          angular.forEach(data, function(item){
            $scope.pizzas.push(item);
          });
        });
      };
      update();
      $scope.$on('update', update);
    });