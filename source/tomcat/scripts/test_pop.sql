\connect personal_assistant

INSERT INTO public.users(email, username, pass_hash, display_name, userpic, interests, created_at)
    VALUES ('user6@gmail.com', 'user6', 'df34hvddg678ccvv', 'Васил', 'link', 'интересно', NOW()::timestamp);
