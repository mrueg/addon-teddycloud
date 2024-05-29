server {
    listen {{ .interface }}:{{ .port }} default_server;

    include /etc/nginx/includes/server_params.conf;
    include /etc/nginx/includes/proxy_params.conf;

    location / {
        allow   127.0.0.1;
	allow   172.30.32.2;
        deny    all;

        absolute_redirect off;

        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header Origin "";
        proxy_redirect '/' $http_x_ingress_path/;
        sub_filter_types text/xml text/css text/javascript application/javascript;
	sub_filter 'href="/' 'href="$http_x_ingress_path/';
        sub_filter '<script src="/' '<script src="$http_x_ingress_path/';
        sub_filter '<script defer="defer" src="/' '<script defer="defer" src="$http_x_ingress_path/';
	sub_filter "top.location.href='" "top.location.href='$http_x_ingress_path";
	sub_filter "fetch('/v1/" "fetch('$http_x_ingress_path/v1/";
	sub_filter "fetch('/api/" "fetch('$http_x_ingress_path/api/";
        sub_filter 'fetch("/api/' 'fetch("$http_x_ingress_path/api/';
        sub_filter "fetch(`/api/" "fetch(`$http_x_ingress_path/api/";
        sub_filter '.concat("/web",' '.concat("$http_x_ingress_path/web",';
        sub_filter 'REACT_APP_TEDDYCLOUD_WEB_BASE:"/web"' 'REACT_APP_TEDDYCLOUD_WEB_BASE:"$http_x_ingress_path/web"';
        sub_filter 'PUBLIC_URL:"/web"' 'PUBLIC_URL:"$http_x_ingress_path/web"';
        sub_filter 'p="/web/"' 'p="$http_x_ingress_path/web/"';
        sub_filter 'basename:"/web"' 'basename:"$http_x_ingress_path/web"';
        sub_filter 'n="/api/' 'n="$http_x_ingress_path/api/';
        sub_filter 'concat("","/api/' 'concat("","$http_x_ingress_path/api/';
        sub_filter 'path:"/api/' 'path:"$http_x_ingress_path/api/';
        sub_filter 'path:"/v1/' 'path:"$http_x_ingress_path/v1/';
        sub_filter 'path:"/reverse/v1/time' 'path:"$http_x_ingress_path/reverse/v1/time';
	sub_filter "concat('', '/api" "concat('', '$http_x_ingress_path/api";
	sub_filter_once off;

        proxy_pass http://backend;
	}
}
