certificateModule = angular.module('certificates', []);

certificateModule.service('tokenService', function(){
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

certificateModule.factory('httpRequestInterceptor',['tokenService', function (tokenService) {
	
		
	  return {
	    request: function (config) {
	    	var t = tokenService.getToken();
	      config.headers['X-XSRF-Token'] = t;
	       return config;
	    }
	  };
	}]);
//'$httpProvider', 'tokenService', '$http', 
certificateModule.config(function ($httpProvider) {
	
	
	$httpProvider.interceptors.push('httpRequestInterceptor');
	});



certificateModule.controller("CertificateController", function($http,$scope, $log){
	var control = this;
	$scope.revokeRequest={};
	control.certificate = {};
	control.certificateRequest = {};
	control.certReqs = [];
	control.certificate.validFrom = new Date();
	control.result = "";
	$scope.alias="";
	$scope.certificateDTO={};
	$scope.found=false;
	$scope.aliasForRevoke="";
	$scope.aliasForCheck="";
	control.importCert = {};
	
	this.generate = function(){
		if(control.certificate.validFrom < control.certificate.validTo){
			$http.post('/certificates/genCertificate', control.certificate).then(function success(response) {
				if(response.data === 'ok'){
					toastr.success('Certificate successfully generated!');
					control.certificate = {};
				}else{
					toastr.error(response.data);
				}
			}, function error(response) {
				control.result = "Unknown error ocurred."
			});
		}else{
			toastr.error('Invalid date entries!');
		}
	};
	
	this.generateRequest = function(){
		$http.post('/certificates/genCertificateRequest', control.certificateRequest).then(function success(response) {
			if(response.data === 'ok'){
				toastr.success('Certificate request successfully generated!');
				control.certificateRequest = {};
			}else{
				toastr.error(response.data);
			}
		}, function error(response) {
			control.result = "Unknown error ocurred."
		});
	};
	
	this.resetIssuer = function(){
		if(control.certificate.selfSigned){
			control.certificate.issuerAlias = "";
		}
	}
	
	this.readCertificate=function(){
		$log.log("Read certificate");
		var path='/certificates/certificate/'+$scope.alias;
		$log.log("Putanja "+ path);
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				$log.log("Uspijesno pronalazenje sertifikata");
				$scope.certificateDTO=response.data;
				$scope.alias="";
				$scope.found=true;
			}, 
			function errorCallback(response){
				$scope.found=false;
			}
		);
	}
	
	this.revokeCertificate=function(){
		$log.log("Revoke certificate.");
		var path='/certificates/revokeCertificate/'+$scope.aliasForRevoke;
		$log.log("Path "+path);
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				$log.log("SuccessCallback");
				$log.log("ResponseStatus: "+response.status);
				$log.log("ResponseData: "+response.data);
				if(response.status===200){
					toastr.success(response.data);
				}else{
					toastr.error("Tekst neki");
				}
			},
			function errorCallback(response){
				$log.log("ËRROR");
			}
		);
	}
	
	this.checkCertificateStatus=function(){
			$log.log("Check sertificate status");
			var path='/certificates/status/'+ $scope.aliasForCheck;
			$log.log("path "+ path);
			$http({
				method:'GET',
				url: path
			}).then(
			function successCallback(response){
				$log.log("Success callback");
				if(response.status===200){
					toastr.success(response.data);
				}else{
					toastr.error(response.data);
				}
			}, function errorCallback(response){
				$log.log("Error");
			}
			);
	}
	
	this.loadRequests = function() {
		$http({
			method: 'GET',
			url: '/certificates/getCertificateRequests'
		}).then(function success(response) {
			control.certReqs = response.data;
		});
	}
			
	this.loadRequests();
	
	this.approveCert = function(certReq){
		$http.get('/certificates/makeCertificate/' + certReq.id).then(function success(response) {
			if(response.data === 'ok'){
				toastr.success('Certificate successfully generated!');
				
				var index = -1;		
				var certReqArr = eval( control.certReqs );
				for( var i = 0; i < certReqArr.length; i++ ) {
					if( certReqArr[i].id === certReq.id ) {
						index = i;
						break;
					}
				}
				if( index === -1 ) {
					toastr["error"]('Something gone wrong.');
				}
				control.certReqs.splice( index, 1 );
			}else{
				toastr.error(response.data);
			}
		}, function error(response) {
			control.result = "Unknown error ocurred."
		});
	}
	
	this.importCertificate = function(){
		$http({
		    method: 'POST',
		    url: '/certificates/importCert',
		    data: control.importCert
		}).
		then(function mySucces(response) {
			if(response.data == 'ok'){
				toastr.success('Certificate successfully imported!');
			}else{
				toastr.error('Uknown error ocurred!');
			}
			
			control.importCert = {};
		});

	}
	this.downloadCertificate = function(){
		$log.log("DOWNLOAD "+ $scope.certificateDTO.serialNumber);
			//findCertificateService.findCertificate($scope.findSerialNumber)
			$http({
				 method: 'GET',
				 url: '/certificates/download/'+$scope.certificateDTO.certificateName,
			})
			.then(function(response){
		        var blob = new Blob([response.data], {type: 'application/x-x509-ca-cert'});
		        saveAs(blob,$scope.certificateDTO.serialNumber+'.cer');
		        $scope.findSerialNumber = "";
			},
			function(response){
				alert("NOT FOUND");
			})
	}
		
	
});

