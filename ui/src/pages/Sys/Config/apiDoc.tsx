import {ApiReferenceReact} from '@scalar/api-reference-react'
import '@scalar/api-reference-react/style.css'

export default () => {
	return (
		<ApiReferenceReact
			configuration={
			[
				{
					sources: [
						{
							title: "User Service",
							url: "/api-proxy/auth/api/v3/api-docs",
							default: true,
						}
					],
					proxyUrl: "/api-proxy/auth/scalar",
					expandAllResponses: true,
					hideClientButton: true,
					orderRequiredPropertiesFirst: true,
					expandAllModelSections: false,
				},
				{
					sources: [
						{
							title: "Test Service",
							url: "/api-proxy/admin/api/v3/api-docs",
							default: true,
						}
					],
					proxyUrl: "/api-proxy/admin",
					expandAllResponses: true,
					hideClientButton: true,
					orderRequiredPropertiesFirst: true,
					expandAllModelSections: false,
				}
			]
		}
		/>
	);
};
