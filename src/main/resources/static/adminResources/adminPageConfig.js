var administratorBanke = angular.module('administratorBanke', ['ui.router']);

administratorBanke.config(function($stateProvider) {
    
	var homeState = {
		    name: 'home',
		    url: '/home',
		    template: '<h3>hello world!</h3>'
		  }

	var permisijeState = {
		    name: 'permisije',
		    url: '/permisije',
		    templateUrl: 'roleIpermisije/Permisije.html'
		  }

	var roleState = {
		    name: 'role',
		    url: '/role',
		    templateUrl: 'roleIpermisije/Role.html'
		  }

	
	var registracijaState = {
		    name: 'registracija',
		    url: '/registracija',
		    templateUrl: 'registracija/registracija.html'
		  }

	$stateProvider.state(homeState);
	$stateProvider.state(permisijeState);
	$stateProvider.state(roleState);
	$stateProvider.state(registracijaState);
});