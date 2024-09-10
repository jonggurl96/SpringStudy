INSERT INTO tc_authority (
	                         auth_code, auth_name, upper_code
                         )
VALUES (
	       'ROLE_ANONYMOUS', '모든 사용자', NULL
       ),
       (
	       'ROLE_USER', '로그인한 사용자', 'ROLE_ANONYMOUS'
       ),
       (
	       'ROLE_A', 'A권한 사용자', 'ROLE_USER'
       ),
       (
	       'ROLE_B', 'B권한 사용자', 'ROLE_USER'
       ),
       (
	       'ROLE_ADMIN', '관리자', 'ROLE_ANONYMOUS'
       );



INSERT INTO tn_users (
	                     user_no, user_id, username, password
                     )
VALUES (
	       'USER_9999999999999', 'admin', '관리자', 'admin'
       ),
       (
	       'USER_0000000000001', 'userDTO', 'User', 'userDTO'
       ),
       (
	       'USER_0000000000002', 'usera', 'User A', 'usera'
       ),
       (
	       'USER_0000000000003', 'userb', 'User B', 'userb'
       );



INSERT INTO tn_menu (
	                    menu_no, menu_nm, menu_url
                    )
VALUES (
	       'MENU_0000000000001', '메인페이지', '/main'
       ),
       (
	       'MENU_0000000000002', '암호화 기능', '/api/enc'
       ),
       (
	       'MENU_0000000000003', '권한 A 테스트', '/api/a/test'
       ),
       (
	       'MENU_0000000000004', '권한 B 테스트', '/api/b/test'
       );



INSERT INTO tn_user_authority (
	                              user_no, author_code
                              )
VALUES (
	       'USER_9999999999999', 'ROLE_ANONYMOUS'
       ),
       (
	       'USER_9999999999999', 'ROLE_ADMIN'
       ),
       (
	       'USER_0000000000001', 'ROLE_ANONYMOUS'
       ),
       (
	       'USER_0000000000001', 'ROLE_USER'
       ),
       (
	       'USER_0000000000002', 'ROLE_ANONYMOUS'
       ),
       (
	       'USER_0000000000002', 'ROLE_USER'
       ),
       (
	       'USER_0000000000002', 'ROLE_A'
       ),
       (
	       'USER_0000000000003', 'ROLE_ANONYMOUS'
       ),
       (
	       'USER_0000000000003', 'ROLE_USER'
       ),
       (
	       'USER_0000000000003', 'ROLE_B'
       );



INSERT INTO tn_menu_authority (
	                              menu_no, author_code
                              )
VALUES (
	       'MENU_0000000000001', 'ROLE_ANONYMOUS'
       ),
       (
	       'MENU_0000000000001', 'ROLE_USER'
       ),
       (
	       'MENU_0000000000001', 'ROLE_A'
       ),
       (
	       'MENU_0000000000001', 'ROLE_B'
       ),
       (
	       'MENU_0000000000001', 'ROLE_ADMIN'
       ),
       (
	       'MENU_0000000000002', 'ROLE_USER'
       ),
       (
	       'MENU_0000000000002', 'ROLE_A'
       ),
       (
	       'MENU_0000000000002', 'ROLE_B'
       ),
       (
	       'MENU_0000000000002', 'ROLE_ADMIN'
       ),
       (
	       'MENU_0000000000003', 'ROLE_A'
       ),
       (
	       'MENU_0000000000003', 'ROLE_ADMIN'
       ),
       (
	       'MENU_0000000000004', 'ROLE_B'
       ),
       (
	       'MENU_0000000000004', 'ROLE_ADMIN'
       );