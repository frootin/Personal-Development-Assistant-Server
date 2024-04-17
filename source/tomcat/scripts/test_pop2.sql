\connect personal_assistant

INSERT INTO public.users(email, username, pass_hash, display_name, userpic, interests, created_at)
    VALUES ('user7@gmail.com', 'user7', 'df34hvddg678ccvv', 'Васил', 'link', 'интересно', NOW()::timestamp);
