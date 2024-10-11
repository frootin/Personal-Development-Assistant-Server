\connect personal_assistant

CREATE TABLE IF NOT EXISTS public.day_notes
(
   id bigserial PRIMARY KEY,
   user_id bigint,
   assigned_day date UNIQUE,
   note_text text,
   FOREIGN KEY (user_id) REFERENCES public.users (id) MATCH SIMPLE
       ON UPDATE NO ACTION
       ON DELETE CASCADE
);

INSERT INTO public.day_notes(user_id, assigned_day, note_text)
    VALUES (1, '2024-10-11'::date, 'Заметка 1'),
           (1, '2024-10-12'::date, 'Заметка 2');
