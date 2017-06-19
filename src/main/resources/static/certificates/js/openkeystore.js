keystoreModule = angular.module('keystore', []);

keystoreModule.service('tokenService', function(){
	var token = "";
	
	var setToken = function(vrednost){
		token = vrednost;
	}
	
	var getToken = function(){
		return token;
	}

	return {
		setToken : setToken,
		getToken : getToken
	};
	
});

keystoreModule.factory('httpRequestInterceptor',['tokenService', function (tokenService) {
	
		
	  return {
	    request: function (config) {
	    	var t = tokenService.getToken();
	      config.headers['X-XSRF-Token'] = t;
	       return config;
	    }
	  };
	}]);
//'$httpProvider', 'tokenService', '$http', 
keystoreModule.config(function ($httpProvider) {
	
	
	$httpProvider.interceptors.push('httpRequestInterceptor');
});

keystoreModule.controller("KeystoreController",['$http', '$scope', '$log', '$window', 'tokenService', function($http,$scope, $log, $window, tokenService){
	var control = this;
	
	control.keystore = {};
	
	this.goBack = function(){
    	$http.get('/goBack').
		then(function mySucces(response) {
			
				$window.location.href=response.data.username;
		});
    };
	
    this.logoff = function(){
		
		$http.get('/logoff').
		then(function mySucces(response) {
			
				//$window.location.href="http://localhost:8080/authentification/login.html";
				toastr.success("IZLOGOVAN");
				$window.location.href=response.data.username;
		});
	};
	
	this.open = function(){
		$http({
		    method: 'POST',
		    url: '/certificates/openKeystore',
		    data: control.keystore
		}).
		then(function mySucces(response) {
			if(response.data.id == 1){
				$window.location.href=response.data.url;
				
				return;
			}else if(response.data.id == -1){
				toastr.error('Keystore is already opened!');
			}else if(response.data.id == -2){
				$window.location.href=response.data.url;
				
				return;
			}else if(response.data.id == -3){
				toastr.error('Incorrect keystore name and/or password!');
			}
		});
	}
	
	$scope.init = function(){
	
		
		$http.get('/special/getSafeToken').
		then(function mySucces(response) {
			
			
			if(angular.equals(response.data.name, 'OHNO')){
				$window.location.href=response.data.value;
			}
			
			tokenService.setToken(response.data.value);
		});
		
		
		
    };
}]);