package br.diego.leilaoonline.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import br.diego.leilaoonline.infra.converter.AtomicBooleanConverter;
import br.diego.leilaoonline.infra.converter.CurrencyUnitConverter;
import br.diego.leilaoonline.infra.converter.MonetaryAmountConverter;
import br.diego.leilaoonline.lance.model.Lance;
import br.diego.leilaoonline.leilao.model.Leilao;
import br.diego.leilaoonline.usuario.model.Usuario;

public class CriadorDeSessao {

	private static Metadata metadata;
	private static SessionFactory sessionFactory;
	private static final MetadataSources sources;
	
	static  {

		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.applySetting("hibernate.dialect", "org.hibernate.dialect.HSQLDialect")
				.applySetting("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver")
				.applySetting("hibernate.connection.url", "jdbc:hsqldb:leilaoonline.db;shutdown=true")
				.applySetting("hibernate.connection.username", "sa")
				.applySetting("hibernate.connection.password", "")
				.applySetting("hibernate.hbm2ddl.auto", "create-drop")
				.applySetting("hibernate.show_sql", "true")
				.applySetting("hibernate.format_sql", "true")
				.applySetting("javax.persistence.schema-generation.database.action", "drop-and-create")
				.applySetting("javax.persistence.schema-generation.scripts.action", "drop-and-create")
				.applySetting("javax.persistence.schema-generation.scripts.create-target", "src/main/resources/META-INF/create.ddl")
				.applySetting("javax.persistence.schema-generation.scripts.drop-target", "src/main/resources/META-INF/drop.ddl")
				.applySetting("javax.persistence.schema-generation.create-source", "metadata")
				.applySetting("javax.persistence.schema-generation.drop-source", "metadata")
				.applySetting("javax.persistence.sql-load-script-source", "src/main/resources/META-INF/import.sql")
				.build();

		sources = new MetadataSources(registry)
				.addAnnotatedClass(Lance.class)
				.addAnnotatedClass(Leilao.class)
				.addAnnotatedClass(Usuario.class);		
	}

	public Session session() {
		if (sessionFactory == null) {
			sessionFactory = getMetadata().buildSessionFactory();
		}
		return sessionFactory.openSession();
	}

	private Metadata getMetadata() {
		if (metadata == null) {
			metadata = sources.getMetadataBuilder()
					.applyAttributeConverter(AtomicBooleanConverter.class)
					.applyAttributeConverter(MonetaryAmountConverter.class)
					.applyAttributeConverter(CurrencyUnitConverter.class)
					.build();
		}
		return metadata;
	}
}
