var app = angular.module('socialNetwork');

app.service('UserService', function () {

    var userService = {};

    userService.convertDate = function (person) {
        person.birthDate = new Date(person.birthDate);
        person.created = new Date(person.created);
        return person;
    };

    return userService;

});

app.service('MessageService', ['$timeout', function ($timeout) {

    var msgService = {};

    msgService.scrollElement = function (id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    };

    msgService.convertDateAndMultiLines = function (messages) {
        messages.forEach(function (message) {
            message.body = message.body.replace(/\\n/g, '\n');
            message.posted = new Date(message.posted);
        });
        return messages
    };

    msgService.addMessage = function (sender, recipient, text) {
        return {
            "sender": sender,
            "recipient": recipient,
            "body": text
        };
    };

    return msgService;

}]);

app.service('FeedService', ['$timeout', function ($timeout) {

    var feedService = {};

    feedService.scrollElement = function (id) {
        $timeout(function () {
            var scroller = document.getElementById(id);
            scroller.scrollTop = scroller.scrollHeight;
        }, 0, false)
    };

    feedService.convertDateAndMultiLines = function (feedList) {
        feedList.forEach(function (feedItem) {
            feedItem.body = feedItem.body.replace(/\\n/g, '\n');
            feedItem.posted = new Date(feedItem.posted);
        });
        return feedList
    };

    feedService.addRecord = function (sender, recipient, text) {
        return {
            "sender": sender,
            "recipient": recipient,
            "body": text
        };
    };

    return feedService;

}]);

app.service('AuthService', ['$http', '$route', '$rootScope', 'URL', function ($http, $route, $rootScope, URL) {

    var defAvatar = URL +'/images/avatars/anonymous.png';

    this.profileId = null;
    this.avatar = defAvatar;
    this.authenticated = false;

    this.load = function () {
        this.update();

        var context = this;

        $http.get(URL + '/api/login',{data:""}).then(function (response) {
            var profile = response.data;
            context.create(profile.id, profile.pageAvatar);
            context.update();
        });
    };

    this.create = function (profileId, avatar) {
        this.profileId = profileId;
        this.avatar = !!avatar ? avatar : defAvatar;
        this.authenticated = true;
        this.update();
    };

    this.destroy = function () {
        this.profileId = null;
        this.avatar = defAvatar;
        this.authenticated = false;
        this.update();
    };

    this.update = function () {
        $rootScope.avatar = this.avatar;
        $rootScope.profileId = this.profileId;
        $rootScope.authenticated = this.authenticated;
        $route.reload();
    };

}]);
