'use strict';

/**
 * @ngdoc overview
 * @name pizzashackAdminWebApp
 * @description
 * # pizzashackAdminWebApp
 *
 * Main module of the application.
 */
angular
  .module('pizzashackAdminWebApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngAnimate',
    'ui.bootstrap',
    'pizzaAdmin'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/pizza', {
        templateUrl: 'views/pizza/pizzaMain.html',
        controller: 'PizzaCtrl'
      })
      .when('/pizza/create', {
        templateUrl: 'views/pizza/pizzaCreate.html',
        controller: 'PizzaCreateCtrl'
      })
      .when('/pizza/info/:id', {
        templateUrl: 'views/pizza/pizzaInfo.html',
        resolve: {
                    id: function ($q, $route, pizzaAdminApi) {
                    return pizzaAdminApi.getOne($route.current.params.id);
                    }
                },
        controller: 'PizzaDetailCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
