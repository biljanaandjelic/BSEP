//var app = angular.module('certificates', []);

certificateModule.controller("CertificateRevokeAndGetStatus", function($http,$scope, $log){
	$scope.banks=[];
	$scope.bank={};
	$scope.revokeReques={};
	$scope.revokeRequests=[];
	this.nesto="Neki string probe radi.";
	$scope.nesto1="tako nesto";
	this.init=function(){
		
		
		var path='/allBanks';
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				
				angular.forEach(response.data, function (element, index) {
			
					$scope.banks.push(element);
					
				});
			
				
			}, function errorCallback(response){
				$log.log("Error callback");
			}
		);
	
		var path="/certificates/revokeRequest";
		$http({
			method:'GET',
			url: path
			
		}).then(
			function successCallback(response){
				angular.forEach(response.data, function (element, index) {
					$log.log("Komentar "+ element.comment);
					$scope.revokeRequests.push(element);
					
				});
				//$log.log("Broj zabiljezenih zahtjeva banke "+$scope.revokeRequests.length)
			}, function errorCallback(response){
				
			}
		);
	};
	
	this.sendRevocationRequest=function(){
		$log.log("Send revoke request ");
		$log.log("Revoke request alias: "+ $scope.revokeRequest.alias);
		
		$log.log("Selected bank "+ $scope.bank.name);
		
		var path="/certificates/createRevokeRequest";
		$scope.revokeRequest.serialNumber=$scope.revokeRequest.alias;
		$http({
			method: 'POST',
			url: path,
			data: $scope.revokeRequest
		}).then(
			function successCallback(response){
				$log.log(response.data);
				$scope.revokeRequest=[];
				toastr.success("Zahtjev je poslan.");
			}, 
			function errorCallback(response){
				$scope.revokeRequest=[];
				toastr.error("Zahtjev nije poslan.");
			}
		);
	}
	
	this.acceptRequest=function(id){
		var path="/certificates/revokeRequest/"+id+"/accept";
		$http({
			method: 'DELETE',
			url:path
		}).then(
			function successCallback(response){
				$log.log(response.data);
				var index=getIndex(id,$scope.revokeRequests);
				$log.log("index "+index);
				$scope.revokeRequests.splice(index,1);
			},
			function errorCallback(response){
				$log.log(response.data);
			}
		);
	}
	
	this.declineRequest=function(id){
		var path="/certificates/revokeRequest/"+id+"/decline";
		$http({
			method: 'DELETE',
			url: path
		}).then(
			function successCallback(response){
				$log.log(response.data);
				var index=getIndex(id,$scope.revokeRequests);
				$scope.revokeRequests.splice(index,1);
			}, 
			function errorCallback(response){
				$log.log(response.data);
			}
		);
	}
	this.getCertificateStatus=function(bank,id){
		var path="/ocspRespone/"+id;
		$log.log("Putanja koja se gadja "+ path);
		
	}
	getIndex=function(id, collection){
		index=-1;
		 for( var i = 0; i < collection.length; i++ ) {
                if( collection[i].id === id ) {
                    index = i;
					//return index;
                    break;
                 }
          }
		  return index;
	}
});