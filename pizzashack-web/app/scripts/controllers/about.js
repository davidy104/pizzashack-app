'use strict';

/**
 * @ngdoc function
 * @name pizzashackWebApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the pizzashackWebApp
 */
angular.module('pizzashackWebApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
