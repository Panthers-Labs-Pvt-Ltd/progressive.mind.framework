insert into edl_data_controls (control_name, control_desc, created_ts, created_by, updated_ts, updated_by, active_flg)
values 	( 'Completeness', 'A measure of the degree to which data attributes meet the expectation that they should contain populated values in a data set.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Comprehensiveness', 'A measure of the degree to which a dataset used for a report contains all material areas that are relevant to that report.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Immutability', 'A measure of the degree to which a dataset has not changed post its creation.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Conformity', 'A measure of the degree to which data values are stored or presented in a format that is consistent with the specified standards.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Accuracy', 'A measure of the degree to which data values are valid when compared to an authoritative source of references (Golden Source data which can be internal or external to the bank.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Uniqueness', 'A measure of the degree to which a data attribute, or combination of attributes, meet the expectation that no duplicate values are present in a data set.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Timeliness', 'A measure of the degree to which data are accessible for use as specified and in the time frame in which it is expected.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y'),
			( 'Consistency', 'A measure of the equivalence of comparable data attributes stored across two or more data sets, or in different locations within the same dataset, at comparable points in time.', CURRENT_TIMESTAMP, 'Prashant',null,null,'Y') ON CONFLICT DO NOTHING;
