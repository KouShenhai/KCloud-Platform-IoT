import moment from 'moment';

export function trim(str: string) {
	return str?.trim();
}

export function formatTimestamp(timestamp?: number | string | null) {
	if (
		timestamp == null ||
		(typeof timestamp === 'string' && !timestamp.trim())
	) {
		return undefined;
	}
	const date = moment(Number(timestamp));
	return date.isValid()
		? date.locale('en').format('YYYY-MM-DD HH:mm:ss.SSS')
		: undefined;
}
