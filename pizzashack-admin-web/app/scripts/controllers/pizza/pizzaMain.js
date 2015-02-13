'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaCtrl', function ($scope, $resource, pizzaAdminApi) {
      $scope.pizzas = [];
      $scope.pageoffset = 0;
      $scope.totalCount = 0;
      $scope.pagesize = 3;
      $scope.querypizzaname = '';

      $scope.previousuri = '';
      $scope.nexturi = '';

      var update = function(){
        pizzaAdminApi.paginate($scope.pageoffset,$scope.pagesize,$scope.querypizzaname).then(function(data){
          $scope.totalCount = data.totalCount;
          var pizzaList = data.content;
          if(pizzaList.length > 0){
            $scope.pageoffset = $scope.pageoffset + 1;
          }
          console.log('aft totalCount:',$scope.totalCount);
          console.log('aft pageoffset:',$scope.pageoffset);
          angular.forEach(pizzaList, function(item){
            $scope.pizzas.push(item);
          });
        });
      };

      update();

      $scope.previous = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        if($scope.pageoffset != 0){
          $scope.pageoffset = $scope.pageoffset - 1;
        }
        $scope.pizzas = [];
        update();
      };

      $scope.next = function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.pizzas = [];
        update();
      };

      $scope.$on('update', update);
    });