'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaDetailCtrl', function () {
    })
  .controller('PizzaDetailInfoCtrl', function ($scope, pizzaAdminApi, $routeParams) {
      $scope.id = $routeParams.id;
      $scope.pizzaItem = pizzaAdminApi.currentItem;
});
