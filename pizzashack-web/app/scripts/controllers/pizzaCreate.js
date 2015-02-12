'use strict';

angular.module('pizzashackWebApp')
  .controller('PizzaCreateCtrl', function ($scope, pizzaAdminApi, $log, $filter) {
    $scope.files = [];

    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };

    $scope.formats = ['yyyy-MM-dd', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];


    $scope.setFiles = function(element) {
      $scope.$apply(function() {
        console.log('files:', element.files);
        for (var i = 0; i < element.files.length; i++) {
          $scope.files.push(element.files[i]);
        }
      });
    };
    
  	$scope.create = function () {
  		$log.log($scope.pizzashackName);
    
      $scope.pizzashackCreateDate =$filter('date')($scope.pizzashackCreateDate, 'yyyy-MM-dd hh:mm:ss');
      $log.log($scope.pizzashackCreateDate);
      $log.log($scope.files[0]);
      $log.log($scope.files[0].name);

      pizzaAdminApi.create($scope).then(function(data){
        $log.log(data);
        $scope.createSuccess = true;
        $scope.successMessage = data;
      	
    	});
  	};
});
