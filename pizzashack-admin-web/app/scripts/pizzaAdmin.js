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
            get: function() {
                var ps = $resource('http://localhost:8181/pizzashackApp/admin/pizzashack/list');
                var deferred = $q.defer();
                ps.query(function(data){
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

                // fd.append('imageName',form.file.name);
                // var blob = new Blob([form.file], {type : "image/png"});
                // fd.append('image',blob);
                // fd.append('name',form.pizzashackName);
                // fd.append('description',form.pizzashackDescription);
                // fd.append('createDate',form.pizzashackCreateDate);
                // fd.append('amount',form.pizzashackAmount);
                // fd.append('price',form.pizzashackPrice);

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
