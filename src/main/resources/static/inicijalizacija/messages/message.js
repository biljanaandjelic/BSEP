administrator.controller('MessagesController', function($scope, $http, $compile,$log){
	$scope.message={};
	$scope.messages=[];
	var  State={
		VIEW_EDIT: 0,
		ADD : 1,
	    SEARCH : 2
	}
	
	$scope.state=State.VIEW_EDIT;
	this.addClick=function(){
		$log.log("ADD");
		$scope.state=State.ADD;
		$scope.message={};
	}
	
	this.searchClick=function(){
		$scope.state=State.SEARCH;
		
	}
	
	this.firstClick=function(){
		if($scope.state===State.VIEW_EDIT){
			$log.log("First click");
			$scope.activity=$scope.activities[0];
			//$scope.valutaId=selectedValuta.id;
		//	$scope.valuta=selectedValuta;
		
		
		}
	}
	
	this.refresh=function(){
		var path="/messages";
		
		$http({
			method: 'GET',
			url: path
		}).then(
			function successCallback(response){
				
				$scope.messages=response.data;
			},function errorCallback(response){
				
			}
		);
	}
	
	this.init=function(){
		$scope.state=State.VIEW_EDIT;
		this.refresh();
	}
	
	this.commitClick=function(){
		
		
		$log.log("Stanje "+ $scope.state);
		$log.log("Activity "+$scope.activity.code +" name "+$scope.activity.name);
		if($scope.state==State.ADD && check()){
			$log.log("Stanje dodavanja");
			var path="/message";
			$http({
				method: 'PUT',
				url: path,
				data: $scope.message
			}).then(
			function successCallback(response){
				
				$scope.messages.push(response.data);
				$scope.message={};
				
			},
			function errorCallback(response){
				$log.log("Greska-error");
			}
			);
		}/*else if($scope.state==State.VIEW_EDIT && check() ){
			$log.log("stAanje izmjene")
			var path="/activity";
			
			$http({
				method: 'POST',
				url: path,
				data: $scope.activity
			}).then(
			function successCallback(response){
				$log.log("Success");
				var index=findIndexOfValuta($scope.activity.id);
				$scope.activities[index]=response.data;
				
			}, 
			function errorCallback(response){
				$log.log("Error");
			});
		}else if($scope.state==State.SEARCH){
			var path="/activities/"+$scope.activity.code+"/"+$scope.activity.name;
			$http({
				method: 'GET',
				url: path
			}).then(
				function successCallback(response){
					
					$scope.activities=response.data;
					$scope.activity={};
					
				}, 
				function errorCallback(response){
				}
			);
		} */
		else {
			$log.log("Nije obradjeno");
		}
	}
	
	var check=function(){
			$log.log("Vrijednosti koje se provjeravaju "+ $scope.message.code );
			if(angular.equals($scope.message, {})){
				toastr.error('Ne mozete ostaviti polja prazna');
				return false;
			}else if(angular.isUndefined($scope.message.code)){
				
				toastr.error('Oznaka  mora sadrzati tacno 5 karaktera');
				return false;
			}else if(!angular.equals($scope.message.code.trim().length, 5)){
				toastr.error('Oznaka mora da sadrzi 5 karaktera!');
				return false;
			}
		$log.log("TRUE");
		return true;
	} 
});