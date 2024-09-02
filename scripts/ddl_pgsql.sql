CREATE TABLE tc_table_nos
(
	table_name varchar(50) NOT NULL
		CONSTRAINT tc_table_nos_pk
			PRIMARY KEY,
	last_no    integer     NOT NULL
);

COMMENT ON TABLE tc_table_nos IS '테이블 PK 목록';

COMMENT ON COLUMN tc_table_nos.table_name IS '테이블 명';

COMMENT ON COLUMN tc_table_nos.last_no IS '번호';

ALTER TABLE tc_table_nos
	OWNER TO jongg;



CREATE TABLE tn_users
(
	user_no        varchar(20) NOT NULL
		CONSTRAINT tn_users_pk
			PRIMARY KEY,
	user_id        varchar(20) NOT NULL,
	username       varchar(20) NOT NULL,
	password       text        NOT NULL,
	cnt_login_failr integer DEFAULT 0
);

COMMENT ON TABLE tn_users IS '사용자 목록';

COMMENT ON COLUMN tn_users.user_no IS '사용자번호';

COMMENT ON COLUMN tn_users.user_id IS '사용자 ID';

COMMENT ON COLUMN tn_users.username IS '사용자 명';

COMMENT ON COLUMN tn_users.password IS '암호';

COMMENT ON COLUMN tn_users.cnt_login_failr IS '로그인 실패 횟수';

ALTER TABLE tn_users
	OWNER TO jongg;



CREATE TABLE tc_authority
(
	auth_code  varchar(20) NOT NULL
		CONSTRAINT tc_authority_pk
			PRIMARY KEY,
	auth_name  varchar(20) NOT NULL,
	upper_code varchar(20)
);

COMMENT ON TABLE tc_authority IS '권한코드 목록';

COMMENT ON COLUMN tc_authority.auth_code IS '권한코드';

COMMENT ON COLUMN tc_authority.auth_name IS '권한명';

COMMENT ON COLUMN tc_authority.upper_code IS '상위 권한 코드';

ALTER TABLE tc_authority
	OWNER TO jongg;



CREATE TABLE tn_menu
(
	menu_no  varchar(20) NOT NULL
		CONSTRAINT tn_menu_pk
			PRIMARY KEY,
	menu_nm  varchar(50) NOT NULL,
	menu_url varchar(50)
);

COMMENT ON TABLE tn_menu IS '메뉴 목록';

COMMENT ON COLUMN tn_menu.menu_no IS '메뉴 번호';

COMMENT ON COLUMN tn_menu.menu_nm IS '메뉴명';

COMMENT ON COLUMN tn_menu.menu_url IS '메뉴 URL';

ALTER TABLE tn_menu
	OWNER TO jongg;



create table tn_user_authority
(
    user_no     varchar(20) not null
        constraint tn_user_authority_tn_users_user_no_fk
            references tn_users,
    author_code varchar(20) not null
        constraint tn_user_authority_tc_authority_auth_code_fk
            references tc_authority,
    constraint tn_user_authority_pk
        primary key (user_no, author_code)
);

comment on table tn_user_authority is '사용자별 권한 목록';

comment on column tn_user_authority.user_no is '사용자 번호';

comment on column tn_user_authority.author_code is '권한 코드';

ALTER TABLE tc_authority
	OWNER TO jongg;



CREATE TABLE tn_menu_authority
(
	menu_no     varchar(20) NOT NULL
		CONSTRAINT tn_menu_authority_tn_menu_menu_no_fk
			REFERENCES tn_menu,
	author_code varchar(20) NOT NULL
		CONSTRAINT tn_menu_authority_tc_authority_auth_code_fk
			REFERENCES tc_authority,
	CONSTRAINT tn_menu_authority_pk
		PRIMARY KEY (menu_no, author_code)
);

COMMENT ON TABLE tn_menu_authority IS '메뉴별 권한 목록';

COMMENT ON COLUMN tn_menu_authority.menu_no IS '메뉴 번호';

COMMENT ON COLUMN tn_menu_authority.author_code IS '권한 코드';

ALTER TABLE tn_menu_authority
	OWNER TO jongg;

