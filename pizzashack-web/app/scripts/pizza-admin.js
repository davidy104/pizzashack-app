
'use strict';

angular.module('pizzaAdmin',[])
    .factory('pizzaAdminApi', function( $resource, $q ){
        var self = {
            currentItem: null,
            getOne: function( id ) {
                var ps = $resource('http://localhost:8181/pizzashackApp/pizzashack/:pizzashackId',{pizzashackId:'@id'});
                var deferred = $q.defer();
                ps.get({pizzashackId:id},function(data){
                    self.currentItem = data;
                    deferred.resolve(data);
                });
                return deferred.promise;
            },
            get: function() {
                var ps = $resource('http://localhost:8181/pizzashackApp/pizzashack/list');
                var deferred = $q.defer();
                ps.query(function(data){
                    deferred.resolve(data);
                });
                return deferred.promise;
            }
        };
        return self;
    });