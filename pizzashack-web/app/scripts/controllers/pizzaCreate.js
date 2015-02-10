'use strict';

angular.module('pizzashackWebApp')
  .controller('PizzaCreateCtrl', function ($scope, pizzaAdminApi, $log) {
    var pizzashack = {
          name : $scope.pizzashack.pizzashackName,
          description  : $scope.pizzashack.description,
          image  : $scope.pizzashack.image
    };
    pizzaAdminApi.create(pizzashack).then(function(data){
      $log.log(data);
    }
});
