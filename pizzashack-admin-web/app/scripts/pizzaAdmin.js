'use strict';

angular.module('pizzaAdmin',[])
    .factory('pizzaAdminApi', function( $resource, $http, $q ){
        var self = {
            currentItem: null,
            getOne: function( id ) {
                var ps = $resource('http://localhost:8181/pizzashackApp/admin/pizzashack/:pizzashackId',{pizzashackId:'@id'});
                var deferred = $q.defer();
                ps.get({pizzashackId:id},function(data){
                    self.currentItem = data;
                    deferred.resolve(data);
                });
                return deferred.promise;
            },
            delete: function( id ) {
                console.log('bef send delete:', id);
                var ps = $resource('http://localhost:8181/pizzashackApp/admin/pizzashack/:pizzashackId');
                var deferred = $q.defer();
                ps.delete({pizzashackId:id},function(){
                });
                return deferred.promise;
            },
            get: function() {
                var ps = $resource('http://localhost:8181/pizzashackApp/admin/pizzashack/list');
                var deferred = $q.defer();
                ps.query(function(data){
                    deferred.resolve(data);
                });
                return deferred.promise;
            },
            paginate: function(pageoffset, pagesize, pizzaname) {
            
                var ps = $resource('http://localhost:8181/pizzashackApp/admin/pizzashack/page');
                var deferred = $q.defer();
                ps.get({pageOffset:pageoffset,pageSize:pagesize,pizzashackName:pizzaname},function(data){
                    deferred.resolve(data);
                });
                return deferred.promise;
            },
            create: function(form){
                var deferred = $q.defer();
                var pizzamodelJson = angular.toJson(form.pizzaModel);
                console.log('pizzamodelJson',pizzamodelJson);
                var fd = new FormData();
                fd.append('model', pizzamodelJson);
                fd.append('image',form.file);

                $http.post('http://localhost:8181/pizzashackApp/admin/pizzashack', fd, {
                     transformRequest: angular.identity,
                     headers: {'Content-Type': undefined}
                }).success(function(data){
                     deferred.resolve(data);
                }).error(function () {
                    //params: data, status, headers, config
                    // alert("failed!");
                });
                return deferred.promise;
            }
        };
        return self;
    });
