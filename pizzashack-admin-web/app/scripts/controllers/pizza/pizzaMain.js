'use strict';
'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaCtrl', function ($scope, $resource, pizzaAdminApi, $templateCache) {
      $scope.pizzas;
      $scope.pageoffset = 0;
      $scope.totalCount = 0;
      $scope.currentPage = 1;
      $scope.totalPages = 0;
      $scope.pagesize = 3;
      $scope.querypizzaname = '';
      $scope.nextclass = '';
      $scope.previousclass = 'disabled';


      var update = function(){
        $scope.pizzas = [];
        
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
          } else {
            $scope.nextclass = '';
          }

          if(data.totalPages > data.currentPage ){
            $scope.previousclass = '';
          }  

          if(data.currentPage == 1){
            $scope.previousclass = 'disabled';
          }
           
          angular.forEach(pizzaList, function(item){
            $scope.pizzas.push(item);
          });
        });
      };

      update();
      $scope.$on('update', update);

      $scope.deleteOne = function($event, deleteItem) {
        console.log('deleteId:',deleteItem.pizzashackId);
        $event.preventDefault();
        $event.stopPropagation();
        pizzaAdminApi.delete(deleteItem.pizzashackId).then(function(data){
        });
        $templateCache.removeAll();
        $scope.$emit('update');
      };

      $scope.previous = function($event) {
        if($scope.previousclass != 'disabled'){
          $event.preventDefault();
          $event.stopPropagation();
          $scope.pageoffset = $scope.pageoffset - 1;
          $scope.$emit('update');
        } 
      };

      $scope.next = function($event) {
        if($scope.nextclass != 'disabled'){
          console.log('next start:');
          $event.preventDefault();
          $event.stopPropagation();
          $scope.pageoffset = $scope.pageoffset + 1;
          console.log('next pageoffset:',$scope.pageoffset);
          $scope.$emit('update');
        }         
      };
    });