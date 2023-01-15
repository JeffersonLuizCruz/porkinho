create table tb_order (
	id bigint auto_increment not null,
	code varchar(20) null,
	subtotal numeric(19,2) null,
	freight_rate numeric(19,2) null,
	total_amount numeric(19,2) null,
	address_cep varchar(15) null,
	address_coplement varchar(150) null,
	address_district varchar(25) null,
	address_number varchar(5) null,
	address_street varchar(20) null,
	address_city_id bigint null,
	status varchar(10) null,
	create_at datetime null,
	confirmation_at datetime null,
	cancellation_at datetime null,
	delivery_at datetime null,
	payment_method_id bigint not null,
	restaurant_id bigint not null,
	user_client_id bigint not null,
	
	primary key (id),
	constraint fk_address_city_order_id foreign key (address_city_id) references city(id),
	constraint payment_method_order_id foreign key (payment_method_id) references payment_method(id),
	constraint fk_restaurant_order_id foreign key (restaurant_id) references restaurant(id),
	constraint fk_user_client_order_id foreign key (user_client_id) references customer(id)
)engine=InnoDB default charset=utf8;