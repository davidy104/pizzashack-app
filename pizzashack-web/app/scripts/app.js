'use strict';

/**
 * @ngdoc overview
 * @name pizzashackWebApp
 * @description
 * # pizzashackWebApp
 *
 * Main module of the application.
 */
angular
  .module('pizzashackWebApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngAnimate',
    'pizzaAdmin'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/pizza', {
        templateUrl: 'views/pizza-main.html',
        controller: 'PizzaCtrl'
      })
      .when('/pizza/info/:id', {
        templateUrl: 'views/pizza-info.html',
        resolve: {
                    id: function ($q, $route, pizzaAdminApi) {
                    return pizzaAdminApi.getOne($route.current.params.id);
                    }
                },
        controller: 'PizzaDetailCtrl'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
