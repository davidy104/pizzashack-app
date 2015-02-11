
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
                var fd = new FormData();
                fd.append('image',form.pizzashackImage);
                fd.append('name',form.pizzashackName);
                fd.append('description',form.pizzashackDescription);
                fd.append('createDate',form.pizzashackCreateDate);
                fd.append('amount',form.pizzashackAmount);

                $http.post('http://localhost:8181/pizzashackApp/admin/pizzashack/upload', fd, {
                     transformRequest: angular.identity,
                     headers: {'Content-Type': undefined}
                }).success(function(data){
                     deferred.resolve(data);
                });

                // var ps = $resource('http://localhost:8181/pizzashackApp/pizzashack/upload',fd,{
                //         transformRequest: angular.identity,
                //         headers: {'Content-Type': undefined}
                // })
    
                // ps.save(function(data){
                //     deferred.resolve(data);
                // });
                return deferred.promise;
            }
        };
        return self;
    });
