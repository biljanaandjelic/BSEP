certificateModule = angular.module('certificates', []);

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
	
});

