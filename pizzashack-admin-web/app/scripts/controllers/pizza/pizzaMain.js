'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaCtrl', function ($scope, $resource, pizzaAdminApi) {
      $scope.pizzas = [];
      $scope.pageoffset = 0;
      $scope.totalCount = 0;
      $scope.currentPage = 1;
      $scope.totalPages = 0;
      $scope.pagesize = 3;
      $scope.querypizzaname = '';
      $scope.nextclass = '';
      $scope.previousclass = 'disabled';


      var update = function(){
        pizzaAdminApi.paginate($scope.pageoffset,$scope.pagesize,$scope.querypizzaname).then(function(data){
          console.log('aft totalCount:',data.totalCount);
          console.log('aft currentPage:',data.currentPage);
          console.log('aft totalPages:',data.totalPages);

          $scope.totalCount = data.totalCount;
          $scope.currentPage = data.currentPage;
          $scope.totalPages = data.totalPages;
          var pizzaList = data.content;

          if(data.totalPages == data.currentPage){
            $scope.nextclass = 'disabled';
          } else if(data.totalPages > data.currentPage && $scope.pageoffset > 0){
            $scope.previousclass = '';
          } else if ($scope.pageoffset == 0){
            $scope.previousclass = 'disabled';
          }
          $scope.pageoffset = data.currentPage -1;
          
         
          angular.forEach(pizzaList, function(item){
            $scope.pizzas.push(item);
          });
        });
      };

      update();

      $scope.previous = function($event) {
        if($scope.previousclass != 'disabled'){
          $event.preventDefault();
          $event.stopPropagation();
          $scope.pageoffset = $scope.pageoffset - 1;
          $scope.pizzas = [];
          update();
        } else {
          $scope.nextclass = '';
        }
      };

      $scope.next = function($event) {
        if($scope.nextclass != 'disabled'){
          console.log('next start:');
          $event.preventDefault();
          $event.stopPropagation();
          console.log('cur pageoffset:',$scope.pageoffset);
          $scope.pageoffset = $scope.pageoffset+1;
          console.log('next pageoffset:',$scope.pageoffset);
          $scope.pizzas = [];
          update();
        } else {
          $scope.previousclass = '';
        }
        
      };

      // $scope.$on('update', update);
    });