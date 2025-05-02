\connect personal_assistant

SELECT id, task_name, time_start, time_end, status FROM tasks WHERE (date_start < CURRENT_DATE OR date_start IS NULL) AND 0 < (date_end - CURRENT_DATE) AND (date_end - CURRENT_DATE) < 3;
