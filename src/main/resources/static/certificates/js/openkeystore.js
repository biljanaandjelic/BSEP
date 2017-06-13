certificateModule.controller("KeystoreController",['$http', '$scope', '$log', '$window', function($http,$scope, $log, $window){
	var control = this;
	control.keystore = {};
	
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
	
}]);