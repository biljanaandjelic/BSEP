//var app = angular.module('certificates', []);
certificateModule.controller("CertificateStatus", function($http,$scope, $log){
	this.init=function(){
		$log.log("CertificateStatus");
		$scope.banks=[];
		$scope.bank={};
		var path='/allBanks';
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				$log.log("Broj banki "+ response.data.length);
				angular.forEach(response.data, function (element, index) {
					
					$scope.banks.push(element);
					
				});
			
				
			}, function errorCallback(response){
				$log.log("Error callback");
			}
		);
		$log.log("*************************");
		$log.log("Citanje zahtjeva iz baze");
		$log.log("*************************");
	}
	this.getCertificateStatus=function(bank,alias){
		$log.log("Bank "+ bank.name);
		$log.log("Potvrda unosa");
		var path="/ocspResponse/"+alias;
		$log.log("Putanja koja se gadja "+ path);
		$http({
			method: 'POST',
			url: path,
			data: bank
		}).then(
		function successCallback(response){
			$log.log("Response OK");
			$log.log(response.data.status);
			if(response.data.status=="MALFORMEDREQUEST"){
				$log.log("Prepoznaje statuse");
				toastr.error("Status: "+ response.data.status);
			}
			if(response.data.status=="SUCCESSFUL"){
				var responseBytes=response.data.respnseBytes;
				var nekiOdgovori=responseBytes.responseData;
				angular.forEach(nekiOdgovori.responses, function (element, index) {
					toastr.success("Status: "+ element.certStatus);
					//$scope.banks.push(element);
					$log.log("Status: "+ element.certStatus);
					
				});
				//response.data.responseBytes.responseData.responses
			}
		}, function errorCallback(response){
			$log.log("fail");
		}
		);
	}
});