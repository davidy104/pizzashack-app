'use strict';

angular.module('pizzashackAdminWebApp')
  .controller('PizzaCreateCtrl', function ($scope, pizzaAdminApi, $log, $filter) {
    $scope.file;

    $scope.pizzaModel = {
        pizzaName: "",
        description: "",
        price: "",
        icon: "",
        amount: "",
        createTime: "",
    };

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
          $scope.file = element.files[i];
          // var r = new FileReader();
          // r.onloadend = function(e){
          //   $scope.imageBytes = e.target.result;
          // }
          // $scope.imageBytes = r.readAsArrayBuffer(element.files[i]);
        }
      });
    };
    
  	$scope.create = function () {
      $scope.pizzaModel.createTime =$filter('date')($scope.pizzaModel.createTime, 'yyyy-MM-dd hh:mm:ss');
      $log.log($scope.pizzaModel.createTime);
      $log.log($scope.file);

      $scope.pizzaModel.icon = $scope.file.name;
       $log.log($scope.pizzaModel);

      pizzaAdminApi.create($scope).then(function(data){
        $log.log(data);
        $scope.createSuccess = true;
        $scope.successMessage = data;
    	});
  	};
});
