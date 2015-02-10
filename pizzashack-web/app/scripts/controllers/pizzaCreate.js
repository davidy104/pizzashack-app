'use strict';

angular.module('pizzashackWebApp')
  .controller('PizzaCreateCtrl', function ($scope, pizzaAdminApi, $log) {

  	$scope.create = function () {
		$log.log($scope.pizzashackName);
    	pizzaAdminApi.create($scope).then(function(data){
      	$log.log(data);
    	});
  	};

  	
});
