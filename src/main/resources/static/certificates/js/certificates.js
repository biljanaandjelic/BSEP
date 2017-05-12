var app = angular.module('certificates', []);

app.controller("TabController", function(){
    this.tab = 1;

    this.isSet = function(checkTab) {
      return this.tab === checkTab;
    };

    this.setTab = function(setTab) {
      this.tab = setTab;
    };
});

app.controller("CertificateController", function($http,$scope, $log){
	var control = this;
	control.certificate = {};
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
});



