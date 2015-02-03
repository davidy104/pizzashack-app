'use strict';

angular.module('pizzashackWebApp')
  .controller('PizzaCtrl', function ($scope, $resource, $log, pizzaAdminApi) {
      $scope.pizzas = [];
      var update = function(){
        pizzaAdminApi.get().then(function(data){
          angular.forEach(data, function(item){
            // $log.log(item);
            // $log.log(item.pizzashackId);
            // $log.log(item.pizzaName);
            $scope.pizzas.push(item);
          });
        });
      };
      update();
      $scope.$on('update', update);
    });
